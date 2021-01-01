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

	private NdPoint targetPoint;
	private final List<LAN> preferredLAN = new ArrayList<>();
	private NdPoint home;
	private int ticksSpentAtHome;
	private int ticksSpentAtLAN;
	private int waitingTicks = -1;
	private LAN targetLAN;

	@Override
	public void onTick() {
		if (home == null) {
			init();
		}
		if (waitingTicks == -1) {
			if (targetPoint == null) {
				var currentLAN = person.getConnectedLAN();
				if (currentLAN.isPresent() && currentLAN.get().equals(targetLAN)) {
					// I'm just arrived in a LAN
					targetPoint = home;
					waitingTicks = ticksSpentAtLAN + RandomHelper.nextIntFromTo(0, 3);
					targetLAN = null;
				} else {
					// I'm just arrived at home
					targetLAN = preferredLAN.get(RandomHelper.nextIntFromTo(0, preferredLAN.size() - 1));
					targetPoint = RepastUtils.space.getLocation(targetLAN);
					waitingTicks = ticksSpentAtHome + RandomHelper.nextIntFromTo(0, 3);
				}
			} else {
				moveTowardsTarget();
			}
		} else {
			waitingTicks--;
			if (waitingTicks == 0) {
				waitingTicks = -1;
			}
		}
	}

	private void init() {
		var params = RunEnvironment.getInstance().getParameters();

		this.home = RepastUtils.space.getLocation(person);

		var numLANs = Math.max(1,
				RandomHelper.createNormal(params.getInteger("meanPrefLANs"), params.getInteger("stdPrefLANs")).nextInt());
		var totLANs = RepastUtils.getAllLANsInGrid().size();
		for (var i = 0; i < numLANs; i++) {
			// Note that the same LAN could be sampled more times
			var LANIndex = RandomHelper.nextIntFromTo(0, totLANs - 1);
			preferredLAN.add(RepastUtils.getAllLANsInGrid().get(LANIndex));
		}

		var normalDistrHome = RandomHelper.createNormal(params.getInteger("meanTicksWaitingHome"), params.getInteger("stdTicksWaitingHome"));
		this.ticksSpentAtHome = Math.max(0, normalDistrHome.nextInt());

		var normalDistrLAN = RandomHelper.createNormal(params.getInteger("meanTicksWaiting"), params.getInteger("stdTicksWaiting"));
		this.ticksSpentAtLAN = Math.max(0, normalDistrLAN.nextInt());
	}

	private void moveTowardsTarget() {
		var personCoords = RepastUtils.space.getLocation(person).toDoubleArray(new double[2]);
		var targetCoords = targetPoint.toDoubleArray(new double[2]);

		for (int i = 0; i < 2; i++) {
			double difference = personCoords[i] - targetCoords[i];
			if (difference > 0) {
				personCoords[i] -= 0.5;
			} else {
				personCoords[i] += 0.5;
			}
		}

		RepastUtils.moveTo(person, personCoords[0], personCoords[1]);

		if (RepastUtils.space.getDistance(RepastUtils.space.getLocation(person), targetPoint) <= 0.6) {
			targetPoint = null;
		}
	}
}
