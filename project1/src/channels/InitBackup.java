package channels;




public class InitBackup implements Runnable {
    private int replicationDegree;
    BackupChannel channel;
    private int senderID;
    private String path;
    private Message request;



    public InitBackup(int replicationDegree, BackupChannel channel, int senderID, String path){
        this.replicationDegree = replicationDegree;
        this.channel = channel;
        this.senderID = senderID;
        this.path = path;
    }

    @Override
    public void run(){
        
    }
}