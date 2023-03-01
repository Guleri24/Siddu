import java.util.*;

public class ElectedMembersSelector {
    
    // List of all available members in the distributed system
    private List<Member> members;

    // Constructor to initialize the list of members
    public ElectedMembersSelector(List<Member> members) {
        this.members = members;
    }

    // Method to select the elected members based on time and trust
    public List<Member> selectElectedMembers() {
        
        // Sort the members by their time and trust
        Collections.sort(members, new MemberComparator());
        
        // Select the top half of the members based on their rank
        int size = members.size() / 2;
        List<Member> electedMembers = new ArrayList<>(members.subList(0, size));

        return electedMembers;
    }

    // Comparator to compare members based on their time and trust
    private class MemberComparator implements Comparator<Member> {
        
        @Override
        public int compare(Member m1, Member m2) {
            // Sort by time first
            if (m1.getTime() != m2.getTime()) {
                return Long.compare(m1.getTime(), m2.getTime());
            }
            // Sort by trust next
            else {
                return Integer.compare(m2.getTrust(), m1.getTrust());
            }
        }
    }
    
    // Inner class representing a member in the distributed system
    private static class Member {
        
        private long time;
        private int trust;
        
        public Member(long time, int trust) {
            this.time = time;
            this.trust = trust;
        }

        public long getTime() {
            return time;
        }

        public int getTrust() {
            return trust;
        }
    }
    
    // Example usage
    public static void main(String[] args) {
        
        List<Member> members = new ArrayList<>();
        members.add(new Member(100, 5));
        members.add(new Member(200, 4));
        members.add(new Member(150, 3));
        members.add(new Member(300, 2));
        members.add(new Member(250, 1));
        
        ElectedMembersSelector selector = new ElectedMembersSelector(members);
        List<Member> electedMembers = selector.selectElectedMembers();
        
        System.out.println("Elected members:");
        for (Member member : electedMembers) {
            System.out.println("Time: " + member.getTime() + ", Trust: " + member.getTrust());
        }
    }
    */
}
