import time
import warnings
from itertools import cycle, islice

import matplotlib.pyplot as plt
import matplotlib.pyplot
import numpy as np
import pandas as pd

plt.figure(figsize=(8 * 2 + 1, 5))
plt.subplots_adjust(
    left=0.15, right=0.98, bottom=0.2, top=0.99, wspace=0.05, hspace=0.02
)

dmscan_data = [
    ("circles_clusters.csv", [-1.5,1.5], [-1.5,1.5]),
    ("moons_clusters.csv", [-1.5,2.5], [-1,1.5]),
    ("varied_clusters.csv", [0,12], [-9.5,1]),
    ("aniso_clusters.csv", [-5.5,3], [-3, 5.5]),
    ("blobs_clusters.csv", [0.3,12.5], [-9.7, 0.3]),
    ("no_structures_clusters.csv", [-0.25,1.25], [-0.25,1.25])
]

correct_data = [
    ("circles_dataset_correct.csv", [-1.5,1.5], [-1.5,1.5]),
    ("moons_dataset_correct.csv", [-1.5,2.5], [-1,1.5]),
    ("varied_correct.csv", [0,12], [-9.5,1]),
    ("aniso_correct.csv", [-5.5,3], [-3, 5.5]),
    ("blobs_correct.csv", [0.3,12.5], [-9.7, 0.3]),
    ("dmscan_data/no_structures_clusters.csv", [-0.25,1.25], [-0.25,1.25])
]

COLORS = ["#377eb8",
        "#ff7f00",
        "#4daf4a",
        "#f781bf",
        "#a65628",
        "#984ea3",
        "#999999",
        "#123456",
        "#654321",
        "#4dbf8a",
        "#379fb8",
        "#000000"]

plot_num = 1

for file, x_bounds, y_bounds in dmscan_data:
    
    plt.subplot(2, 6, plot_num)

    df = pd.read_csv(f"dmscan_data/{file}")

    feature_columns = df.columns[:-1]
    cluster_column = df.columns[-1]
    clusters = df[cluster_column].unique()

    for _cluster in clusters:
        cluster_data = df[df[cluster_column] == _cluster]
        plt.scatter(cluster_data[feature_columns[0]], cluster_data[feature_columns[1]], s=10, color=COLORS[_cluster])


    plt.xlim(x_bounds[0], x_bounds[1])
    plt.ylim(y_bounds[0], y_bounds[1])
    plt.xticks(())
    plt.yticks(())
    plot_num += 1


for file, x_bounds, y_bounds in correct_data:
    
    plt.subplot(2, 6, plot_num)

    df = pd.read_csv(f"{file}")
    feature_columns = df.columns[:-1]
    cluster_column = df.columns[-1]
    clusters = df[cluster_column].unique()

    for _cluster in clusters:
        cluster_data = df[df[cluster_column] == _cluster]
        plt.scatter(cluster_data[feature_columns[0]], cluster_data[feature_columns[1]], s=10, color=COLORS[_cluster])


    plt.xlim(x_bounds[0], x_bounds[1])
    plt.ylim(y_bounds[0], y_bounds[1])
    plt.xticks(())
    plt.yticks(())
    plot_num += 1

plt.text(-9.5, 2, 'DMSCAN', fontsize = 22)
plt.text(-9.5, 0.5, 'Correct\nClassification', fontsize = 22)

plt.savefig("evaluate.png")
