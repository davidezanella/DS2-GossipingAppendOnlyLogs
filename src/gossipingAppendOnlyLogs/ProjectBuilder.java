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
	@Override
	public Context<Object> build(Context<Object> context) {
		context.setId("GossipingAppendOnlyLogs");
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context,
				new RandomCartesianAdder<Object>(), new repast.simphony.space.continuous.WrapAroundBorders(), 50, 50);
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(
				new WrapAroundBorders(), new SimpleGridAdder<Object>(), true, 50, 50));

		RepastUtils.grid = grid;

		Parameters params = RunEnvironment.getInstance().getParameters();

		int numLANs = 3;
		for (int i = 0; i < numLANs; i++) {
			var id = "LAN" + i;
			LAN lan = new LAN(id);
			context.add(lan);
		}

		int numPeople = 10;
		for (int i = 0; i < numPeople; i++) {
			var id = "Person" + i;
			Person p = new Person(id, CryptographyUtils.generateKeys());
			context.add(p);
		}
		
		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) pt.getX(), (int) pt.getY());
		}
		
		//int maxTicks = params.getInteger("stopAt");
		//RunEnvironment.getInstance().endAt(maxTicks);

		return context;
	}
}
