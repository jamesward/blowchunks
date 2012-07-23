package controllers;

import akka.actor.Cancellable;
import akka.util.Duration;
import play.libs.Akka;
import play.mvc.Results;

import java.util.concurrent.TimeUnit;

public class ChunkBlower extends Results.StringChunks {

    private final int secondsBetweenChunks;
    
    private final RunnableWithOut blocker;

    public ChunkBlower(int secondsBetweenChunks, RunnableWithOut blocker) {
        this.secondsBetweenChunks = secondsBetweenChunks;
        this.blocker = blocker;
    }

    @Override
    public void onReady(final Out<String> out) {

        Cancellable tick = Akka.system().scheduler().schedule(Duration.Zero(),
                Duration.create(secondsBetweenChunks, TimeUnit.SECONDS),
                new Runnable() {
                    public void run() {
                        out.write(" ");
                    }
                }
        );

        blocker.out = out;
        blocker.run();

        tick.cancel();

        out.close();
    }

    public static abstract class RunnableWithOut implements Runnable {

        public Out<String> out;

    }
}
