package channels;




public class InitBackup implements Runnable{
    private int replicationDegree;
    Channel channel;
    private int senderID;
    private String path;



    public InitBackup(int replicationDegree, Channel channel, int senderID, String path){
        this.replicationDegree = replicationDegree;
        this.channel = channel;
        this.senderID = senderID;
        this.path = path;
    }
}