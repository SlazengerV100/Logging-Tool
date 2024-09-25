package nz.ac.wgtn.swen301.a3.server;

import java.util.ArrayList;
import java.util.List;

public class Persistency {
    public static List<Log> DB = new ArrayList<>();

    public static void reset() {
        DB = new ArrayList<>();
    }
}
