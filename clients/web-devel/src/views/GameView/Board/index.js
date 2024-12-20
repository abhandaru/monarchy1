import * as React from 'react';
import writeSelect from '~/api/writeSelect';
import styles from './index.css';
import Tile from '~/views/GameView/Tile';
import { useSelector, useDispatch } from 'react-redux';

const Board = (props) => {
  const { gameId, playerId, currentPlayerId, currentSelection, tiles } = props;

  // Fetch selection for current selection, if any. Only do this once on mount.
  React.useEffect(() => {
    if (currentSelection != null) {
      const query = { gameId, point: currentSelection };
      writeSelect(query);
    }
  }, []);

  // Tile generation logic
  const maxRow = tiles.reduce((z, t) => Math.max(t.point.i, z), 0);
  const size = 'calc(' + (100 / (maxRow + 1)) + 'vmin - 16px)';
  const grid = [...Array(maxRow + 1).keys()]
    .map(i => tiles.filter(_ => _.point.i === i))
    .map(_ => _.sort((a, b) => a.point.j - b.point.j));

  return (
    <div>{grid.map((row, i) =>
      <div key={i} className={styles.boardRow}>{row.map((tile, j) =>
        <Tile
          key={j}
          playerId={playerId}
          currentPlayerId={currentPlayerId}
          tile={tile}
          size={size}
        />
      )}</div>
    )}</div>
  );
};

export default Board;
