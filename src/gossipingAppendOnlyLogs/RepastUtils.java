package gossipingAppendOnlyLogs;

import gossipingAppendOnlyLogs.actors.LAN;
import gossipingAppendOnlyLogs.actors.Person;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.grid.Grid;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RepastUtils {

    public static Grid<Object> grid;

    public static List<LAN> getAllLANsInGrid(Object actor){
        return getAllActorsInGrid(actor, LAN.class);
    }

    public static List<Person> getAllPeopleInGrid(Object actor){
        return getAllActorsInGrid(actor, Person.class);
    }

    private static <T> List<T> getAllActorsInGrid(Object actor, Class<T> clazz) {
        if (grid == null) {
            throw new IllegalStateException("'grid' has not been initialized");
        }
        var pt = grid.getLocation(actor);
        var extentX = grid.getDimensions().getWidth() / 2;
        var extentY = grid.getDimensions().getHeight() / 2;
        GridCellNgh<T> nghCreator = new GridCellNgh<T>(grid, pt, clazz, extentX, extentY);
        return nghCreator
                .getNeighborhood(true)
                .stream()
                .flatMap(cell -> StreamSupport.stream(cell.items().spliterator(), false))
                .collect(Collectors.toList());
    }

    public static double getDistance(Person person, LAN lan) {
        return grid.getDistance(
                grid.getLocation(person),
                grid.getLocation(lan)
        );
    }
}
