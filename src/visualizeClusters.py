import matplotlib.pyplot
import pandas as pd
import matplotlib.pyplot as plt


def read_and_plot_clusters(filename):
    df = pd.read_csv(filename)
    
    feature_columns = df.columns[:-1]
    cluster_column = df.columns[-1]
    
    clusters = df[cluster_column].unique()
    
    colors = matplotlib.pyplot.get_cmap('viridis', len(clusters))
    
    for cluster in clusters:
      cluster_data = df[df[cluster_column] == cluster]
      plt.scatter(cluster_data[feature_columns[0]], cluster_data[feature_columns[1]], color=colors(cluster), label=f'Cluster {cluster}')
    
    plt.title('Clusters')
    plt.xlabel('Feature 1')
    plt.ylabel('Feature 2')
    plt.legend()
    plt.show()


if __name__ == '__main__':
  filename = 'clusters.csv'
  read_and_plot_clusters(filename)