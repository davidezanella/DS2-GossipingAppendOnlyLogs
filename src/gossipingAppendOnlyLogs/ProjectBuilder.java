package gossipingAppendOnlyLogs;

import gossipingAppendOnlyLogs.actors.LAN;
import gossipingAppendOnlyLogs.actors.Person;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class ProjectBuilder implements ContextBuilder<Object> {

    private static final int GRID_SIZE = 50;

    private final ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
    private final GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);

    private Context<Object> context;

    private ContinuousSpace<Object> space;

    private Grid<Object> grid;

    @Override
    public Context<Object> build(Context<Object> context) {
        this.context = context;
        context.setId("GossipingAppendOnlyLogs");

        this.space = spaceFactory.createContinuousSpace(
                "space",
                context,
                new RandomCartesianAdder<Object>(),
                new repast.simphony.space.continuous.WrapAroundBorders(),
                GRID_SIZE,
                GRID_SIZE
        );
        this.grid = gridFactory.createGrid(
                "grid",
                context,
                new GridBuilderParameters<Object>(new WrapAroundBorders(), new SimpleGridAdder<Object>(),
                        true,
                        GRID_SIZE,
                        GRID_SIZE
                )
        );

        RepastUtils.grid = grid;
        RepastUtils.context = context;
        RepastUtils.space = space;

        var params = RunEnvironment.getInstance().getParameters();

        createLANs(params);
        createPeople(params);
        randomlyMoveAllAgents();

        int maxTicks = params.getInteger("stopAt");
        RunEnvironment.getInstance().endAt(maxTicks);

        return context;
    }

    private void createLANs(Parameters params){
        int numLANs = 3;
        for (int i = 0; i < numLANs; i++) {
            var id = "LAN" + i;
            LAN lan = new LAN(id);
            context.add(lan);
        }
    }

    private void createPeople(Parameters params) {
        int numPeople = 10;
        for (int i = 0; i < numPeople; i++) {
            var id = "Person" + i;
            Person p = new Person(id, CryptographyUtils.generateKeys());
            context.add(p);
        }
    }

    private void randomlyMoveAllAgents() {
        for (Object obj : context) {
            NdPoint pt = space.getLocation(obj);
            RepastUtils.moveTo(obj, pt.getX(), pt.getY());
        }
    }
}
