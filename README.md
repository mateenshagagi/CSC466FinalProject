# CSC466FinalProject

https://github.com/jbrownlee/Datasets
https://github.com/jbrownlee/Datasets/blob/master/adult-all.csv

https://www.kaggle.com/datasets/uciml/red-wine-quality-cortez-et-al-2009


https://towardsdatascience.com/part-2-gradient-descent-and-backpropagation-bf90932c066a

TODO:
1. Label core/noise/edge points (maybe using opacity)
2. Evaluator
3. 3d graphs
4. Presentation


DBScan class
- take data -> output clusters
- parameters epsilon and minPts

Evaluation class
clusters -> number

evaluate method
for each cluster
sort all points in cluster
find largest distance between consecutive points
return average of this distance for all clusters

Data class
array

Optimizer class?
Iterates over epsilon and minPts to find optimal clusters

Data visualization 


Distance function:
L2 norm

Something to keep track of visited points

def explore(a, visted_points, clusters):
    add a to visited
    
    neighbors = find neighbors(pt)
    if # neighbors >= min points:
        create cluster with neighbors
        for each neighbor:
         if neighbor is core point:
            explore neighbor
        else:
            neighbor is edge point
            mark neighbor as visited
    else:
        label point a as noise

def DBScan():
    visited_points = ()
    clusters = []
    while points that havent been visited:
        pick random point a
        explore a

find neighbors (epsilon, pt):
    neighbors = []
    for each pt in points:
        distance = L2norm
        if distance <= epsilon:
            add to neighbors
    return neighbors