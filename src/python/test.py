# Read in the CSV file
input_file_path = '../datasets/moons.csv'
output_file_path = '../datasets/moons.csv'

with open(input_file_path, 'r') as file:
    lines = file.readlines()

# Process each line to remove the third value
modified_lines = []
for line in lines:
    values = line.strip().split(',')
    if len(values) >= 3:
        modified_line = ','.join(values[:2])  # Join only the first two values
        modified_lines.append(modified_line)
    else:
        modified_lines.append(line.strip())

# Write the modified lines to a new file
with open(output_file_path, 'w') as file:
    for modified_line in modified_lines:
        file.write(modified_line + '\n')