import matplotlib.pyplot
import pandas as pd
import matplotlib.pyplot as plt
import sys
import pathlib
import plotly.express as px


def plot3d(filename):
  df = pd.read_csv(filename)
  noise_df = pd.read_csv(pathlib.Path(filename).stem + "_noise.csv")

  columns = df.columns
  fig = px.scatter_3d(df, x=columns[0], y=columns[1], z=columns[2], color='cluster')
  fig.add_scatter3d(x=noise_df[columns[0]], y=noise_df[columns[1]], z=noise_df[columns[2]], mode='markers', marker=dict(color='black'), name='Noise')
  
  fig.show()


def read_and_plot_clusters(filename):
    df = pd.read_csv(filename)
    
    feature_columns = df.columns[:-1]
    cluster_column = "cluster"
    clusters = df[cluster_column].unique()
    
    colors = matplotlib.pyplot.get_cmap('viridis', len(clusters))

    for cluster in clusters:
      cluster_data = df[df[cluster_column] == cluster]
      plt.scatter(cluster_data[feature_columns[0]], cluster_data[feature_columns[1]], color=colors(cluster), label=f'Cluster {cluster}')
    
    noise_df = pd.read_csv(pathlib.Path(filename).with_name(pathlib.Path(filename).stem + "_noise.csv"))
    noise_df_feature_columns = noise_df.columns
    if (len(noise_df) > 0):
      plt.scatter(noise_df[noise_df_feature_columns[0]], noise_df[noise_df_feature_columns[1]], color="black", label="Noise")
    
    plt.title('Clusters')
    plt.xlabel('Feature 1')
    plt.ylabel('Feature 2')
    plt.legend()
    plt.show()


if __name__ == '__main__':
  if (len(sys.argv) > 2 and sys.argv[2] == "3d"):
    plot3d(sys.argv[1])
  else:
    read_and_plot_clusters(sys.argv[1])