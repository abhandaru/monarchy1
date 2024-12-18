export const compare = (a, b) =>
  a.i === b.i && a.j === b.j;

export const add = (a, b) =>
  ({i: a.i + b.i, j: a.j + b.j});

export const magnitude = (v) =>
  Math.sqrt(v.i * v.i + v.j * v.j)

export const negate = (v) =>
  ({i: -v.i, j: -v.j});

export const distance = (a, b) =>
  magnitude(add(a, negate(b)));

export const meanSquareDistance = (v, vecs) =>
  vecs.map(_ => distance(v, _)).reduce((a, b) => a + b, 0);
