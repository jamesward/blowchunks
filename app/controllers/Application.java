package controllers;

import play.mvc.*;

public class Application extends Controller {

    public static Result index() {

        Chunks<String> chunks = new ChunkBlower(1, new ChunkBlower.RunnableWithOut() {

            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                out.write("Done!");
            }
        });

        return ok(chunks);
    }

}