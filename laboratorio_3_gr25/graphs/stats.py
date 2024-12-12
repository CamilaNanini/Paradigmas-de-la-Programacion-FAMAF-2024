import matplotlib.pyplot as plt
import numpy as np
import sys
from random import seed, shuffle

possible_categories = ['OTHER', 'EVENT', 'ORGANIZATION', 'PERSON', 'LOCATION']

cat_colors = list(plt.cm.tab10(range(0, len(possible_categories))))
# Setear la semilla que determinará la asignación de colores
seed(1)
# Reordenar los colores
shuffle(cat_colors)

color_dict = {possible_categories[i]: cat_colors[i] for i in range(0, len(possible_categories))}

def main(args):
    # Inicializar un diccionario vacío para almacenar los datos
    data = {}
    filename, extension = args[0].split(".")
    with open(filename + "." + extension) as file:
        data_str = file.read()

        # Dividir el texto en líneas
        lines = data_str.strip().split('\n')

        current_category = None
        for line in lines:
            if line.startswith("CATEGORY:"):
                current_category = line.split(":")[1].strip()
                data[current_category] = {}
            else:
                # Procesar la línea para extraer el nombre y el valor
                parts = line.strip().split('(')
                name = parts[0].strip()
                value = int(parts[1].replace(')', '').strip())
                data[current_category][name] = value


    # Preparar los datos para el gráfico de barras
    found_categories = list(data.keys())
    labels = []
    heights = []
    cat_names = []

    for category_index, category in enumerate(found_categories):
        for label, height in data[category].items():
            labels.append(label)
            heights.append(height)
            cat_names.append(category)

    # Crear el gráfico de barras horizontales
    plt.figure(figsize=(9, 14))

    # Coloreo
    bar_colors = [color_dict[cat] for cat in cat_names]
    bars = plt.barh(labels, heights, color=bar_colors, zorder=3.5)

    # Añadir título y etiquetas
    plt.title('Gráfico de Categorías (colores) y Apariciones')
    plt.xlabel('Cantidad de Apariciones')
    plt.ylabel('Entidad nombrada')

    # Añadir leyenda
    plt.legend(bars, found_categories, loc='upper right')

    # Ajustar colores correctamente
    ax = plt.gca()
    leg = ax.get_legend()

    for i in range(0, len(found_categories)):
        leg.legend_handles[i].set_color(color_dict[found_categories[i]])

    # Ajustar la grilla del eje x
    xlimit = int(1.25*max(heights))
    step = int(max(heights)/10)
    # Lo corrijo para que sea un múltiplo de 5
    step = step - step%5
    plt.xticks(np.arange(0, xlimit, step=step))
    plt.grid(True, axis='x', color='lightgray', linestyle='dashed', zorder=1.5)

    # Mostrar gráfico
    plt.tight_layout()
    plt.savefig(filename + ".png")
    plt.show()

if __name__ == "__main__":
    if len(sys.argv) == 2:
        main(sys.argv[1:])
    else:
        print("Invalid inputs")
        print("Usage:   python3 stats.py dataPropiertlyFormatted.txt")