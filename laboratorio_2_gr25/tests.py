import subprocess

"""
-h  -> No toma args
-f  -> Siempre tiene args
-ne -> Sin args == -pf
-pf -> No toma args
-sf -> Se puede llamar siempre con o sin args
"""
# def run_app_with(flagList):
#     for flags in flagList:
#         args = ["make", "run", f"ARGS={flags}"]
#         try:
#             out = subprocess.check_output(args)
#             # print(out.decode("utf-8"))
#         except:
#             print("Something was wrong")

# flagList = ["-h", "-h -ne", "-h -ne cw"]

    
import logging
import itertools

# Set up logging
logging.basicConfig(filename='test.log', level=logging.INFO, format='%(asctime)s %(message)s')

# Define the possible arguments
help_args = ["-h", "--help"]
feed_args = ["-f p12pais", "-f p12eco", "-f lmgral", "-f lmnoti"]
named_entity_args = ["-ne", "-ne specialwordsheuristic", "-ne sp", "-ne acronymwordheuristic", "-ne ac", "-ne capitalizedwordheuristic", "-ne cw"]
print_feed_args = ["-pf"]
stats_format_args = ["-sf cat", "-sf topic"]

# Combine all possible arguments
all_args = help_args + feed_args + named_entity_args + print_feed_args + stats_format_args

# Generate all combinations of arguments
all_combinations = []
for r in range(1, len(all_args) + 1):
    combinations_object = itertools.combinations(all_args, r)
    combinations = [list(comb) for comb in combinations_object]
    all_combinations += combinations

# Run the app with all combinations of arguments
for combination in all_combinations:
    args = ["make", "run", f"ARGS={' '.join(combination)}"]
    try:
        out = subprocess.check_output(args)
        logging.info(out.decode("utf-8"))
    except Exception as e:
        logging.error(f"Error with args {combination}: {e}")