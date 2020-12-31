import os

protocols = ["Open", "Transitive"]

def main():
    repeat_run = 12

    lan_values = [i for i in range(3, 20 + 1, 2)]
    people_values = [i for i in range(10, 50 + 1, 10)]

    count = 1000
    for x in lan_values:
        for y in people_values:
            for protocol in protocols:
                generate_config(count, protocol, x, y, repeat_run)
            count += 1


def generate_config(config_number: int, protocol: str, lan: int, people: int, repeat_run: int):
    folder = f"batch/{config_number}"
    os.makedirs(folder, exist_ok=True)
    generate_properties(folder, protocol, config_number)
    generate_xml(folder, protocol, lan, people, repeat_run)


def generate_properties(folder: str, protocol: str, config_number: int):
    properties = f"""
model.directory=.
host.0.type=LOCAL
batch.param.file=./batch/{config_number}/{protocol}.xml
poll.frequency=0.1
scenario.directory=./GossipingAppendOnlyLogs.rs
param.file=./GossipingAppendOnlyLogs.rs/parameters.xml
key.directory=~/.ssh
host.0.instances=4
output.directory=./output/{config_number}/{protocol}
vm.arguments=
    """
    with open(f"{folder}/{protocol}.properties", "w") as f:
        f.write(properties)


def generate_xml(folder: str, protocol: str, lan: int, people: int, repeat_run: int):
    xml = f"""<?xml version="1.0" ?>
<sweep runs="{str(repeat_run)}">
    <parameter name="stopAt" type="constant" constant_type="int"
        value="1000"></parameter>
    <parameter name="numLANs" type="constant" constant_type="int"
        value="{str(lan)}"></parameter>
    <parameter name="meanTicksWaiting" type="constant"
        constant_type="int" value="5"></parameter>
    <parameter name="numPeople" type="constant"
        constant_type="int" value="{str(people)}"></parameter>
    <parameter name="meanPrefLANs" type="constant"
        constant_type="int" value="1"></parameter>
    <parameter name="stdTicksWaiting" type="constant"
        constant_type="int" value="5"></parameter>
    <parameter name="stdPrefLANs" type="constant"
        constant_type="int" value="1"></parameter>
    <parameter name="motionStrategy" type="constant"
        constant_type="java.lang.String" value="HabitMotion"></parameter>
    <parameter name="synchronizationProtocol" type="constant"
        constant_type="java.lang.String" value="{protocol}Model"></parameter>
    <parameter name="meanTicksWaitingHome" type="constant"
               constant_type="int" value="5"></parameter>
    <parameter name="stdTicksWaitingHome" type="constant"
               constant_type="int" value="2"></parameter>
</sweep>
    """
    with open(f"{folder}/{protocol}.xml", "w") as f:
        f.write(xml)


if __name__ == '__main__':
    main()
