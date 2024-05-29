import matplotlib.pyplot
import pandas as pd
import matplotlib.pyplot as plt
import sys

import plotly.express as px


def plot3d(filename):

  df = pd.read_csv(filename)
  print(df)
  cluster_column = df.columns[-1]
    
  clusters = df[cluster_column].unique()
  fig = px.scatter_3d(df, x='feature1', y='feature2', z='feature3', color='cluster')
  fig.show()


def read_and_plot_clusters(filename):
    df = pd.read_csv(filename)
    
    feature_columns = df.columns[:-1]
    cluster_column = df.columns[-1]
    
    clusters = df[cluster_column].unique()
    max_cluster = max(clusters)
    
    colors = matplotlib.pyplot.get_cmap('viridis', len(clusters))
    
    for cluster in clusters:
      cluster_data = df[df[cluster_column] == cluster]
      plt.scatter(cluster_data[feature_columns[0]], cluster_data[feature_columns[1]], color=colors(cluster), label=f'Cluster {cluster}' if cluster != max_cluster else "Noise")
    
    plt.title('Clusters')
    plt.xlabel('Feature 1')
    plt.ylabel('Feature 2')
    plt.legend()
    plt.show()


if __name__ == '__main__':
  if (sys.argv[2] == "3d"):
    plot3d(sys.argv[1])
  else:
    read_and_plot_clusters(sys.argv[1])