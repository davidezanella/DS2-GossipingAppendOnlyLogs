package gossipingAppendOnlyLogs.motion;

import gossipingAppendOnlyLogs.RepastUtils;
import gossipingAppendOnlyLogs.actors.LAN;
import gossipingAppendOnlyLogs.actors.Person;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.NdPoint;

import java.util.ArrayList;
import java.util.List;

public class HabitMotionStrategy extends MotionStrategy {

	public HabitMotionStrategy(Person person) {
		super(person);
	}
	
	private void init() {		
        var params = RunEnvironment.getInstance().getParameters();
		
		this.home = RepastUtils.space.getLocation(person);

		var numLANs = Math.max(1, 
				RandomHelper.createNormal(params.getInteger("meanPrefLANs"), params.getInteger("stdPrefLANs")).nextInt());
		var totLANs = RepastUtils.getAllLANsInGrid(person).size();
		for (var i = 0; i < numLANs; i++) {
			// Note that the same LAN could be sampled more times
			var LANIndex = RandomHelper.nextIntFromTo(0, totLANs - 1);
			preferredLAN.add(RepastUtils.getAllLANsInGrid(person).get(LANIndex));
		}

		var normalDistr = RandomHelper.createNormal(params.getInteger("meanTicksWaiting"), params.getInteger("stdTicksWaiting"));
		this.ticksSpentAtHome = Math.max(0, normalDistr.nextInt());
		this.ticksSpentAtLAN = Math.max(0, normalDistr.nextInt());
	}

	private NdPoint targetPoint;
	private List<LAN> preferredLAN = new ArrayList<>();
	private NdPoint home;
	private int ticksSpentAtHome;
	private int ticksSpentAtLAN;
	private Integer waitingTicks;

	@Override
	public void onTick() {
		if(home == null)
			init();
		
		if(waitingTicks == null) {
			if (targetPoint == null) {
				var inLAN = preferredLAN.stream()
										.filter(lan -> lan.getConnectedPeople().contains(person))
										.findFirst().orElse(null);
				if (inLAN != null) {
					// I'm just arrived in a LAN
					targetPoint = home;
					waitingTicks = ticksSpentAtLAN + RandomHelper.nextIntFromTo(0, 3);
				}
				else {
					// I'm just arrived at home
					var targetLAN = preferredLAN.get(RandomHelper.nextIntFromTo(0, preferredLAN.size() - 1));
					targetPoint = RepastUtils.space.getLocation(targetLAN);
					waitingTicks = ticksSpentAtHome + RandomHelper.nextIntFromTo(0, 3);
				}
			}
			else {
				moveTowardsTarget();
			}
		}
		else {
			waitingTicks--;
			if (waitingTicks.equals(0))
				waitingTicks = null;
		}
	}

	private void moveTowardsTarget() {
		var personCoords = RepastUtils.space.getLocation(person).toDoubleArray(new double[2]);
		var targetCoords = targetPoint.toDoubleArray(new double[2]);

		for (int i = 0; i < 2; i++) {
			double difference = personCoords[i] - targetCoords[i];
			if (difference > 0) {
				personCoords[i] -= 0.1;
			} else {
				personCoords[i] += 0.1;
			}
		}

		RepastUtils.moveTo(person, personCoords[0], personCoords[1]);

		if (RepastUtils.space.getDistance(RepastUtils.space.getLocation(person), targetPoint) < 0.2) {
			targetPoint = null;
		}
	}
}
