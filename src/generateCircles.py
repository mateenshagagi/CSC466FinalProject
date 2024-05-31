import time
import warnings
from itertools import cycle, islice

import matplotlib.pyplot as plt
import numpy as np

from sklearn import cluster, datasets, mixture
from sklearn.neighbors import kneighbors_graph
from sklearn.preprocessing import StandardScaler
import pandas as pd

# ============
# Generate datasets. We choose the size big enough to see the scalability
# of the algorithms, but not too big to avoid too long running times
# ============
n_samples = 500
seed = 30
noisy_circles = datasets.make_circles(
    n_samples=n_samples, factor=0.5, noise=0.05, random_state=seed
)
data = (pd.DataFrame(noisy_circles[0]))
data.to_csv('circles_dataset.csv', index=False, header=False)
categories = (pd.DataFrame(noisy_circles[1]))
df = pd.concat([data, categories], axis=1)
df.columns = ["x", "y", "cluster"]
df.to_csv('circles_dataset_correct.csv', index=False)

noisy_moons = datasets.make_moons(n_samples=n_samples, noise=0.05, random_state=seed)
data = (pd.DataFrame(noisy_moons[0]))
data.to_csv('moons_dataset.csv', index=False, header=False)
categories = (pd.DataFrame(noisy_moons[1]))
df = pd.concat([data, categories], axis=1)
df.columns = ["x", "y", "cluster"]
df.to_csv('moons_dataset_correct.csv', index=False)