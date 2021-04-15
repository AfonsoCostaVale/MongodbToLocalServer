import mongodb.collectorwriter.MongodbCloudCollector;

public class TerminalController{

    private MongodbCloudCollector mongodbCloudCollector;

    public TerminalController(MongodbCloudCollector mongodbCloudCollector){
        this.mongodbCloudCollector = mongodbCloudCollector;
    }

    public void launch(){
        mongodbCloudCollector.start();
    }

}