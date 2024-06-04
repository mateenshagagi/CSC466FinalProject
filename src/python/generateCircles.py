from sklearn import datasets
import pandas as pd

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


varied = datasets.make_blobs(
    n_samples=n_samples, cluster_std=[1.0, 2.5, 0.5], random_state=seed
)
data = (pd.DataFrame(varied[0]))
data.to_csv('varied_dataset.csv', index=False, header=False)
categories = (pd.DataFrame(varied[1]))
df = pd.concat([data, categories], axis=1)
df.columns = ["x", "y", "cluster"]
df.to_csv('varied_correct.csv', index=False)


# Anisotropicly distributed data
random_state = 170
X, y = datasets.make_blobs(n_samples=n_samples, random_state=random_state)
transformation = [[0.6, -0.6], [-0.4, 0.8]]
X_aniso = np.dot(X, transformation)
aniso = (X_aniso, y)
data = (pd.DataFrame(aniso[0]))
data.to_csv('aniso_dataset.csv', index=False, header=False)
categories = (pd.DataFrame(aniso[1]))
df = pd.concat([data, categories], axis=1)
df.columns = ["x", "y", "cluster"]
df.to_csv('aniso_correct.csv', index=False)



blobs = datasets.make_blobs(n_samples=n_samples, random_state=seed)
data = (pd.DataFrame(blobs[0]))
data.to_csv('blobs_dataset.csv', index=False, header=False)
categories = (pd.DataFrame(blobs[1]))
df = pd.concat([data, categories], axis=1)
df.columns = ["x", "y", "cluster"]
df.to_csv('blobs_correct.csv', index=False)


rng = np.random.RandomState(seed)
no_structure = rng.rand(n_samples, 2), None
data = (pd.DataFrame(no_structure[0]))
data.to_csv('no_structures_dataset.csv', index=False, header=False)
categories = (pd.DataFrame(no_structure[1]))
df = pd.concat([data, categories], axis=1)
df.columns = ["x", "y", "cluster"]
df.to_csv('no_structure_correct.csv', index=False)