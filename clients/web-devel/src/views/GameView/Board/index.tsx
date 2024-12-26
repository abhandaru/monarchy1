import * as React from 'react';
// @ts-ignore (adu) add support for css modules
import styles from './index.css';
import Tile from '~/views/GameView/Tile';

function range(n: number) {
  return Array(n).fill(null).map((_, i) => i);
}

type Props = {
  gameId: string,
  playerId: string,
  currentPlayerId: string,
  tiles: Array<any>,
};

const Board = (props: Props) => {
  const { gameId, playerId, currentPlayerId, tiles } = props;

  // Tile generation logic
  const maxRow = tiles.reduce((z, t) => Math.max(t.point.i, z), 0);
  const size = 'calc(' + (100 / (maxRow + 1)) + 'vmin - 16px)';
  // @ts-ignore
  const grid = range(maxRow + 1)
    .map(i => tiles.filter(_ => _.point.i === i))
    .map(_ => _.sort((a, b) => a.point.j - b.point.j));


  // @ts-ignore
  // console.log(maxRow, Array(maxRow + 1).fill().map((_, i) => i), grid);
  return (
    <div>{grid.map((row, i) =>
      <div key={i} className={styles.boardRow}>{row.map((tile, j) =>
        <Tile
          key={j}
          playerId={playerId}
          currentPlayerId={currentPlayerId}
          tile={tile}
          sizeCss={size}
        />
      )}</div>
    )}</div>
  );
};

export default Board;
