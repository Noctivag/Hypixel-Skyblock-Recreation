package de.noctivag.plugin.performance;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.logging.Level;

/**
 * Async Task Scheduler - Intelligente asynchrone Task-Verwaltung
 * 
 * Verantwortlich für:
 * - Priority-based Task Scheduling
 * - Task Dependency Management
 * - Load Balancing
 * - Task Retry Logic
 * - Performance Monitoring
 * - Resource Management
 */
public class AsyncTaskScheduler {
    
    private final Plugin plugin;
    private final AdvancedThreadPoolManager threadPoolManager;
    
    // Task Queues mit verschiedenen Prioritäten
    private final PriorityBlockingQueue<TaskWrapper> highPriorityQueue = new PriorityBlockingQueue<>();
    private final PriorityBlockingQueue<TaskWrapper> normalPriorityQueue = new PriorityBlockingQueue<>();
    private final PriorityBlockingQueue<TaskWrapper> lowPriorityQueue = new PriorityBlockingQueue<>();
    
    // Task Management
    private final ConcurrentHashMap<String, TaskWrapper> activeTasks = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, TaskDependency> taskDependencies = new ConcurrentHashMap<>();
    
    // Performance Metrics
    private final LongAdder totalTasksScheduled = new LongAdder();
    private final LongAdder totalTasksCompleted = new LongAdder();
    private final LongAdder totalTasksFailed = new LongAdder();
    private final LongAdder totalTasksRetried = new LongAdder();
    
    private final AtomicLong totalExecutionTime = new AtomicLong(0);
    private final AtomicInteger activeTaskCount = new AtomicInteger(0);
    private final AtomicInteger peakTaskCount = new AtomicInteger(0);
    
    // Configuration
    private volatile boolean schedulerEnabled = true;
    private volatile int maxConcurrentTasks = 100;
    private volatile int maxRetryAttempts = 3;
    private volatile long taskTimeout = 30000; // 30 seconds
    
    // Monitoring
    private BukkitTask monitoringTask;
    
    public AsyncTaskScheduler(Plugin plugin, AdvancedThreadPoolManager threadPoolManager) {
        this.plugin = plugin;
        this.threadPoolManager = threadPoolManager;
        
        startTaskMonitoring();
        plugin.getLogger().info("Async Task Scheduler initialized");
    }
    
    /**
     * Plant eine Task mit hoher Priorität
     */
    public CompletableFuture<Void> scheduleHighPriorityTask(String taskId, Runnable task) {
        return scheduleTask(taskId, task, TaskPriority.HIGH, null);
    }
    
    /**
     * Plant eine Task mit normaler Priorität
     */
    public CompletableFuture<Void> scheduleNormalPriorityTask(String taskId, Runnable task) {
        return scheduleTask(taskId, task, TaskPriority.NORMAL, null);
    }
    
    /**
     * Plant eine Task mit niedriger Priorität
     */
    public CompletableFuture<Void> scheduleLowPriorityTask(String taskId, Runnable task) {
        return scheduleTask(taskId, task, TaskPriority.LOW, null);
    }
    
    /**
     * Plant eine Task mit Rückgabewert
     */
    public <T> CompletableFuture<T> scheduleTask(String taskId, Callable<T> task, TaskPriority priority) {
        return scheduleTask(taskId, task, priority, null);
    }
    
    /**
     * Plant eine Task mit Abhängigkeiten
     */
    public CompletableFuture<Void> scheduleTaskWithDependencies(String taskId, Runnable task, 
                                                               TaskPriority priority, String[] dependencies) {
        return scheduleTask(taskId, task, priority, dependencies);
    }
    
    /**
     * Plant eine Task
     */
    private <T> CompletableFuture<T> scheduleTask(String taskId, Object task, TaskPriority priority, String[] dependencies) {
        if (!schedulerEnabled) {
            return CompletableFuture.completedFuture(null);
        }
        
        // Prüfe ob Task bereits existiert
        if (activeTasks.containsKey(taskId)) {
            plugin.getLogger().warning("Task with ID '" + taskId + "' is already scheduled");
            return CompletableFuture.completedFuture(null);
        }
        
        // Erstelle Task Wrapper
        TaskWrapper taskWrapper = new TaskWrapper(taskId, task, priority, dependencies);
        
        // Prüfe Abhängigkeiten
        if (dependencies != null && dependencies.length > 0) {
            TaskDependency dependency = new TaskDependency(taskId, dependencies);
            taskDependencies.put(taskId, dependency);
            
            // Prüfe ob alle Abhängigkeiten erfüllt sind
            if (!areDependenciesMet(dependencies)) {
                plugin.getLogger().info("Task '" + taskId + "' queued due to unmet dependencies");
                @SuppressWarnings("unchecked")
                CompletableFuture<T> typedFuture = (CompletableFuture<T>) taskWrapper.getFuture();
                return typedFuture;
            }
        }
        
        // Füge Task zur entsprechenden Queue hinzu
        addTaskToQueue(taskWrapper);
        
        totalTasksScheduled.increment();
        activeTaskCount.incrementAndGet();
        
        if (activeTaskCount.get() > peakTaskCount.get()) {
            peakTaskCount.set(activeTaskCount.get());
        }
        
        @SuppressWarnings("unchecked")
        CompletableFuture<T> typedFuture = (CompletableFuture<T>) taskWrapper.getFuture();
        return typedFuture;
    }
    
    /**
     * Fügt Task zur entsprechenden Queue hinzu
     */
    private void addTaskToQueue(TaskWrapper taskWrapper) {
        switch (taskWrapper.getPriority()) {
            case HIGH:
                highPriorityQueue.offer(taskWrapper);
                break;
            case NORMAL:
                normalPriorityQueue.offer(taskWrapper);
                break;
            case LOW:
                lowPriorityQueue.offer(taskWrapper);
                break;
        }
        
        // Starte Task-Execution
        executeNextTask();
    }
    
    /**
     * Führt die nächste Task aus
     */
    private void executeNextTask() {
        if (activeTaskCount.get() >= maxConcurrentTasks) {
            return; // Max concurrent tasks reached
        }
        
        TaskWrapper taskWrapper = null;
        
        // Prioritäts-basierte Task-Auswahl
        if (!highPriorityQueue.isEmpty()) {
            taskWrapper = highPriorityQueue.poll();
        } else if (!normalPriorityQueue.isEmpty()) {
            taskWrapper = normalPriorityQueue.poll();
        } else if (!lowPriorityQueue.isEmpty()) {
            taskWrapper = lowPriorityQueue.poll();
        }
        
        if (taskWrapper != null) {
            executeTask(taskWrapper);
        }
    }
    
    /**
     * Führt eine Task aus
     */
    private void executeTask(TaskWrapper taskWrapper) {
        activeTasks.put(taskWrapper.getTaskId(), taskWrapper);
        
        long startTime = System.nanoTime();
        
        CompletableFuture<Void> future = threadPoolManager.executeAsyncTask(() -> {
            try {
                // Führe Task aus
                if (taskWrapper.getTask() instanceof Runnable) {
                    ((Runnable) taskWrapper.getTask()).run();
                } else if (taskWrapper.getTask() instanceof Callable) {
                    Object result = ((Callable<?>) taskWrapper.getTask()).call();
                    taskWrapper.setResult(result);
                }
                
                // Task erfolgreich abgeschlossen
                taskWrapper.complete();
                totalTasksCompleted.increment();
                
            } catch (Exception e) {
                // Task fehlgeschlagen
                plugin.getLogger().log(Level.WARNING, "Task '" + taskWrapper.getTaskId() + "' failed", e);
                handleTaskFailure(taskWrapper, e);
            } finally {
                // Cleanup
                activeTasks.remove(taskWrapper.getTaskId());
                activeTaskCount.decrementAndGet();
                
                long executionTime = System.nanoTime() - startTime;
                totalExecutionTime.addAndGet(executionTime);
                
                // Prüfe abhängige Tasks
                checkDependentTasks(taskWrapper.getTaskId());
                
                // Führe nächste Task aus
                executeNextTask();
            }
        });
        
        // Timeout-Handling
        future.orTimeout(taskTimeout, TimeUnit.MILLISECONDS)
            .exceptionally(throwable -> {
                if (throwable instanceof TimeoutException) {
                    plugin.getLogger().warning("Task '" + taskWrapper.getTaskId() + "' timed out after " + taskTimeout + "ms");
                    handleTaskTimeout(taskWrapper);
                }
                return null;
            });
    }
    
    /**
     * Behandelt Task-Fehler
     */
    private void handleTaskFailure(TaskWrapper taskWrapper, Exception e) {
        totalTasksFailed.increment();
        
        if (taskWrapper.getRetryCount() < maxRetryAttempts) {
            // Retry Task
            taskWrapper.incrementRetryCount();
            totalTasksRetried.increment();
            
            plugin.getLogger().info("Retrying task '" + taskWrapper.getTaskId() + "' (attempt " + 
                taskWrapper.getRetryCount() + "/" + maxRetryAttempts + ")");
            
            // Warte kurz vor Retry
            threadPoolManager.scheduleDelayedTask(() -> {
                addTaskToQueue(taskWrapper);
            }, 1000, TimeUnit.MILLISECONDS);
            
        } else {
            // Max retries reached, fail task
            taskWrapper.completeExceptionally(e);
            plugin.getLogger().severe("Task '" + taskWrapper.getTaskId() + "' failed after " + 
                maxRetryAttempts + " retry attempts");
        }
    }
    
    /**
     * Behandelt Task-Timeout
     */
    private void handleTaskTimeout(TaskWrapper taskWrapper) {
        totalTasksFailed.increment();
        taskWrapper.completeExceptionally(new TimeoutException("Task timed out after " + taskTimeout + "ms"));
    }
    
    /**
     * Prüft abhängige Tasks
     */
    private void checkDependentTasks(String completedTaskId) {
        taskDependencies.values().stream()
            .filter(dependency -> java.util.Arrays.asList(dependency.getDependencies()).contains(completedTaskId))
            .forEach(dependency -> {
                if (areDependenciesMet(dependency.getDependencies())) {
                    // Alle Abhängigkeiten erfüllt, starte Task
                    TaskWrapper taskWrapper = activeTasks.get(dependency.getTaskId());
                    if (taskWrapper != null) {
                        taskDependencies.remove(dependency.getTaskId());
                        addTaskToQueue(taskWrapper);
                    }
                }
            });
    }
    
    /**
     * Prüft ob Abhängigkeiten erfüllt sind
     */
    private boolean areDependenciesMet(String[] dependencies) {
        for (String dependency : dependencies) {
            if (activeTasks.containsKey(dependency)) {
                return false; // Dependency still active
            }
        }
        return true;
    }
    
    /**
     * Startet Task-Monitoring
     */
    private void startTaskMonitoring() {
        monitoringTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            // Log Task Statistics
            if (totalTasksScheduled.sum() > 0) {
                plugin.getLogger().info("Task Scheduler Status: " + 
                    "Active: " + activeTaskCount.get() + 
                    ", Completed: " + totalTasksCompleted.sum() + 
                    ", Failed: " + totalTasksFailed.sum() + 
                    ", Retried: " + totalTasksRetried.sum());
            }
            
            // Cleanup completed dependencies
            taskDependencies.entrySet().removeIf(entry -> 
                !activeTasks.containsKey(entry.getKey()));
            
        }, 20L, 20L * 30); // Every 30 seconds
    }
    
    /**
     * Gibt Task-Statistics zurück
     */
    public TaskStatistics getTaskStatistics() {
        TaskStatistics stats = new TaskStatistics();
        
        stats.setTotalTasksScheduled(totalTasksScheduled.sum());
        stats.setTotalTasksCompleted(totalTasksCompleted.sum());
        stats.setTotalTasksFailed(totalTasksFailed.sum());
        stats.setTotalTasksRetried(totalTasksRetried.sum());
        stats.setActiveTaskCount(activeTaskCount.get());
        stats.setPeakTaskCount(peakTaskCount.get());
        stats.setTotalExecutionTime(totalExecutionTime.get());
        
        // Queue sizes
        stats.setHighPriorityQueueSize(highPriorityQueue.size());
        stats.setNormalPriorityQueueSize(normalPriorityQueue.size());
        stats.setLowPriorityQueueSize(lowPriorityQueue.size());
        
        // Calculate success rate
        long totalTasks = totalTasksCompleted.sum() + totalTasksFailed.sum();
        if (totalTasks > 0) {
            stats.setSuccessRate((double) totalTasksCompleted.sum() / totalTasks);
        }
        
        // Calculate average execution time
        if (totalTasksCompleted.sum() > 0) {
            stats.setAverageExecutionTime(totalExecutionTime.get() / totalTasksCompleted.sum());
        }
        
        return stats;
    }
    
    /**
     * Stoppt den Scheduler
     */
    public void shutdown() {
        plugin.getLogger().info("Shutting down Async Task Scheduler...");
        
        schedulerEnabled = false;
        
        if (monitoringTask != null) {
            monitoringTask.cancel();
        }
        
        // Cancel all active tasks
        activeTasks.values().forEach(taskWrapper -> {
            taskWrapper.completeExceptionally(new InterruptedException("Scheduler shutting down"));
        });
        
        activeTasks.clear();
        taskDependencies.clear();
        
        plugin.getLogger().info("Async Task Scheduler shutdown complete");
    }
    
    /**
     * Task Priority Enum
     */
    public enum TaskPriority {
        HIGH(1),
        NORMAL(2),
        LOW(3);
        
        private final int priority;
        
        TaskPriority(int priority) {
            this.priority = priority;
        }
        
        public int getPriority() {
            return priority;
        }
    }
    
    /**
     * Task Wrapper Klasse
     */
    private static class TaskWrapper implements Comparable<TaskWrapper> {
        private final String taskId;
        private final Object task;
        private final TaskPriority priority;
        private final CompletableFuture<Object> future;
        private final long creationTime;
        
        private int retryCount = 0;
        private Object result;
        
        public TaskWrapper(String taskId, Object task, TaskPriority priority, String[] dependencies) {
            this.taskId = taskId;
            this.task = task;
            this.priority = priority;
            this.future = new CompletableFuture<>();
            this.creationTime = System.currentTimeMillis();
        }
        
        public void complete() {
            future.complete(result);
        }
        
        public void completeExceptionally(Throwable throwable) {
            future.completeExceptionally(throwable);
        }
        
        public void incrementRetryCount() {
            retryCount++;
        }
        
        @Override
        public int compareTo(TaskWrapper other) {
            // Sort by priority (lower number = higher priority)
            int priorityCompare = Integer.compare(this.priority.getPriority(), other.priority.getPriority());
            if (priorityCompare != 0) {
                return priorityCompare;
            }
            
            // Then by creation time (older tasks first)
            return Long.compare(this.creationTime, other.creationTime);
        }
        
        // Getters
        public String getTaskId() { return taskId; }
        public Object getTask() { return task; }
        public TaskPriority getPriority() { return priority; }
        public CompletableFuture<Object> getFuture() { return future; }
        public int getRetryCount() { return retryCount; }
        public void setResult(Object result) { this.result = result; }
    }
    
    /**
     * Task Dependency Klasse
     */
    private static class TaskDependency {
        private final String taskId;
        private final String[] dependencies;
        
        public TaskDependency(String taskId, String[] dependencies) {
            this.taskId = taskId;
            this.dependencies = dependencies;
        }
        
        public String getTaskId() { return taskId; }
        public String[] getDependencies() { return dependencies; }
    }
    
    /**
     * Task Statistics Klasse
     */
    public static class TaskStatistics {
        private long totalTasksScheduled;
        private long totalTasksCompleted;
        private long totalTasksFailed;
        private long totalTasksRetried;
        private int activeTaskCount;
        private int peakTaskCount;
        private long totalExecutionTime;
        private int highPriorityQueueSize;
        private int normalPriorityQueueSize;
        private int lowPriorityQueueSize;
        private double successRate;
        private long averageExecutionTime;
        
        // Getters und Setters
        public long getTotalTasksScheduled() { return totalTasksScheduled; }
        public void setTotalTasksScheduled(long totalTasksScheduled) { this.totalTasksScheduled = totalTasksScheduled; }
        
        public long getTotalTasksCompleted() { return totalTasksCompleted; }
        public void setTotalTasksCompleted(long totalTasksCompleted) { this.totalTasksCompleted = totalTasksCompleted; }
        
        public long getTotalTasksFailed() { return totalTasksFailed; }
        public void setTotalTasksFailed(long totalTasksFailed) { this.totalTasksFailed = totalTasksFailed; }
        
        public long getTotalTasksRetried() { return totalTasksRetried; }
        public void setTotalTasksRetried(long totalTasksRetried) { this.totalTasksRetried = totalTasksRetried; }
        
        public int getActiveTaskCount() { return activeTaskCount; }
        public void setActiveTaskCount(int activeTaskCount) { this.activeTaskCount = activeTaskCount; }
        
        public int getPeakTaskCount() { return peakTaskCount; }
        public void setPeakTaskCount(int peakTaskCount) { this.peakTaskCount = peakTaskCount; }
        
        public long getTotalExecutionTime() { return totalExecutionTime; }
        public void setTotalExecutionTime(long totalExecutionTime) { this.totalExecutionTime = totalExecutionTime; }
        
        public int getHighPriorityQueueSize() { return highPriorityQueueSize; }
        public void setHighPriorityQueueSize(int highPriorityQueueSize) { this.highPriorityQueueSize = highPriorityQueueSize; }
        
        public int getNormalPriorityQueueSize() { return normalPriorityQueueSize; }
        public void setNormalPriorityQueueSize(int normalPriorityQueueSize) { this.normalPriorityQueueSize = normalPriorityQueueSize; }
        
        public int getLowPriorityQueueSize() { return lowPriorityQueueSize; }
        public void setLowPriorityQueueSize(int lowPriorityQueueSize) { this.lowPriorityQueueSize = lowPriorityQueueSize; }
        
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
        
        public long getAverageExecutionTime() { return averageExecutionTime; }
        public void setAverageExecutionTime(long averageExecutionTime) { this.averageExecutionTime = averageExecutionTime; }
    }
}
