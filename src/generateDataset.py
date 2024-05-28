# from https://scikit-learn.org/stable/auto_examples/cluster/plot_dbscan.html
# epsilon = 0.3, minPoints = 10

from sklearn.datasets import make_blobs
from sklearn.preprocessing import StandardScaler
import pandas as pd

centers = [[1, 1], [-1, -1], [1, -1]]
X, labels_true = make_blobs(n_samples=750, centers=centers, cluster_std=0.4, random_state=0)

X = StandardScaler().fit_transform(X)

df = pd.DataFrame(X)
df.to_csv('dataset.csv', index=False, header=False)