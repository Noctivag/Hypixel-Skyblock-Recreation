package de.noctivag.skyblock.engine.temporal.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mayor Election - Represents a mayor election event
 * 
 * Manages candidate selection, voting, and election results
 */
public class MayorElection {
    
    private final String id;
    private final LocalDateTime startTime;
    private final List<String> candidates;
    private ElectionStatus status;
    private final Map<String, Integer> votes;
    private final Map<String, MayorPerk> candidatePerks;
    private LocalDateTime endTime;
    private String winner;
    
    public MayorElection(String id, LocalDateTime startTime, List<String> candidates, ElectionStatus status) {
        this.id = id;
        this.startTime = startTime;
        this.candidates = new ArrayList<>(candidates);
        this.status = status;
        this.votes = new HashMap<>();
        this.candidatePerks = new HashMap<>();
        this.endTime = startTime.plusDays(5); // 5 day election period
        
        // Initialize votes
        for (String candidate : candidates) {
            votes.put(candidate, 0);
        }
        
        // Initialize candidate perks
        initializeCandidatePerks();
    }
    
    /**
     * Initialize candidate perks
     */
    private void initializeCandidatePerks() {
        candidatePerks.put("Diana", new MayorPerk("Mythological Ritual", "Increased chance for mythological creatures"));
        candidatePerks.put("Jerry", new MayorPerk("Jerry's Workshop", "Access to special Jerry items"));
        candidatePerks.put("Marina", new MayorPerk("Fishing Festival", "Enhanced fishing rewards"));
        candidatePerks.put("Paul", new MayorPerk("Dungeon Hub", "Enhanced dungeon rewards"));
        candidatePerks.put("Cole", new MayorPerk("Mining Fiesta", "Enhanced mining rewards"));
        candidatePerks.put("Foxy", new MayorPerk("Auction House", "Reduced auction fees"));
        candidatePerks.put("Aatrox", new MayorPerk("Slayer Quest", "Enhanced slayer rewards"));
        candidatePerks.put("Dante", new MayorPerk("Dark Auction", "Special dark auction items"));
    }
    
    /**
     * Cast a vote for a candidate
     */
    public boolean castVote(String candidate, String voterId) {
        if (status != ElectionStatus.ACTIVE) {
            return false;
        }
        
        if (!candidates.contains(candidate)) {
            return false;
        }
        
        // In a real implementation, you would check if the voter has already voted
        // and store the vote in a database
        
        votes.put(candidate, votes.get(candidate) + 1);
        return true;
    }
    
    /**
     * Get current vote count for a candidate
     */
    public int getVoteCount(String candidate) {
        return votes.getOrDefault(candidate, 0);
    }
    
    /**
     * Get total votes cast
     */
    public int getTotalVotes() {
        return votes.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Get election results
     */
    public Map<String, Integer> getResults() {
        return new HashMap<>(votes);
    }
    
    /**
     * Get winning candidate
     */
    public String getWinningCandidate() {
        if (status != ElectionStatus.COMPLETED) {
            return null;
        }
        
        return votes.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    
    /**
     * Check if election is active
     */
    public boolean isActive() {
        return status == ElectionStatus.ACTIVE && LocalDateTime.now().isBefore(endTime);
    }
    
    /**
     * Check if election has ended
     */
    public boolean hasEnded() {
        return LocalDateTime.now().isAfter(endTime) || status == ElectionStatus.COMPLETED;
    }
    
    /**
     * Complete the election
     */
    public void completeElection() {
        if (hasEnded()) {
            status = ElectionStatus.COMPLETED;
            winner = getWinningCandidate();
        }
    }
    
    /**
     * Get candidate perks
     */
    public MayorPerk getCandidatePerk(String candidate) {
        return candidatePerks.get(candidate);
    }
    
    /**
     * Convert to JSON for Redis storage
     */
    public String toJson() {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        
        json.addProperty("id", id);
        json.addProperty("startTime", startTime.toString());
        json.addProperty("endTime", endTime.toString());
        json.addProperty("status", status.name());
        json.addProperty("winner", winner);
        
        // Add candidates
        JsonArray candidatesArray = new JsonArray();
        for (String candidate : candidates) {
            candidatesArray.add(candidate);
        }
        json.add("candidates", candidatesArray);
        
        // Add votes
        JsonObject votesObject = new JsonObject();
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            votesObject.addProperty(entry.getKey(), entry.getValue());
        }
        json.add("votes", votesObject);
        
        return gson.toJson(json);
    }
    
    /**
     * Create from JSON
     */
    public static MayorElection fromJson(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        
        List<String> candidates = new ArrayList<>();
        JsonArray candidatesArray = obj.getAsJsonArray("candidates");
        for (int i = 0; i < candidatesArray.size(); i++) {
            candidates.add(candidatesArray.get(i).getAsString());
        }
        
        MayorElection election = new MayorElection(
            obj.get("id").getAsString(),
            LocalDateTime.parse(obj.get("startTime").getAsString()),
            candidates,
            ElectionStatus.valueOf(obj.get("status").getAsString())
        );
        
        election.endTime = LocalDateTime.parse(obj.get("endTime").getAsString());
        election.winner = obj.has("winner") ? obj.get("winner").getAsString() : null;
        
        // Restore votes
        JsonObject votesObject = obj.getAsJsonObject("votes");
        for (String candidate : votesObject.keySet()) {
            election.votes.put(candidate, votesObject.get(candidate).getAsInt());
        }
        
        return election;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public LocalDateTime getStartTime() { return startTime; }
    public List<String> getCandidates() { return new ArrayList<>(candidates); }
    public ElectionStatus getStatus() { return status; }
    public void setStatus(ElectionStatus status) { this.status = status; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
    
    /**
     * Election status enum
     */
    public enum ElectionStatus {
        SCHEDULED, ACTIVE, COMPLETED, CANCELLED
    }
    
    /**
     * Mayor Perk class
     */
    public static class MayorPerk {
        private final String name;
        private final String description;
        
        public MayorPerk(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
    }
    
    @Override
    public String toString() {
        return String.format("MayorElection{id='%s', status=%s, candidates=%s, totalVotes=%d}", 
                           id, status, candidates, getTotalVotes());
    }
}
