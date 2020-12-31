import os
import xml.etree.ElementTree as ET

protocols = ["Open", "Transitive"]


def main():
    repeat_run = 12

    x_variable = 'numPeople'
    x_values = [i for i in range(5, 25 + 1, 5)]

    y_variable = 'meanPrefLANs'
    y_values = [i for i in range(1, 10 + 1, 2)]

    count = 2000
    for x in x_values:
        for y in y_values:
            for protocol in protocols:
                generate_config(count, protocol, x_variable, x, y_variable, y, repeat_run)
            count += 1


def generate_config(config_number: int, protocol: str, x_variable: str, x: int, y_variable: str, y: int,
                    repeat_run: int):
    folder = f"batch/{config_number}"
    os.makedirs(folder, exist_ok=True)
    generate_properties(folder, protocol, config_number)
    generate_xml(folder, protocol, x_variable, x, y_variable, y, repeat_run)


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


def generate_xml(folder: str, protocol: str, x_variable: str, x: int, y_variable: str, y: int, repeat_run: int):
    tree = ET.parse('template.xml')
    root = tree.getroot()

    root.set('runs', str(repeat_run))

    update_value(root, 'synchronizationProtocol', f"{protocol}Model")
    update_value(root, x_variable, str(x))
    update_value(root, y_variable, str(y))

    tree.write(f"{folder}/{protocol}.xml")


def update_value(root, variable, value):
    for child in root:
        if child.attrib['name'] == variable:
            child.set('value', value)


if __name__ == '__main__':
    main()
