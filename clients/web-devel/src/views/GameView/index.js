import * as React from 'react';
import classnames from 'classnames';
import NavigationView from '~/components/layout/NavigationView';
import styles from './index.css';
import { gameFetch } from '~/state/actions';
import { useSelector, useDispatch } from 'react-redux';

const Tile = (props) => {
  const { tile, size } = props;
  const { piece } = tile;
  const styleOverride = {width: size, height: size};
  // const classOverride = classnames(styles.boardTile, tile.piece && styles.boardTilePyromancer);
  const classOverride = styles.boardTile;
  return (
    <div style={styleOverride} className={classOverride}>
      {piece ? piece.id : null}
    </div>
  );
}

const Board = (props) => {
  const { tiles } = props;
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
          tile={tile}
          size={size}
        />
      )}</div>
    )}</div>
  );
}

const GameView = (props) => {
  const { gameId } = props.match.params;
  const dispatch = useDispatch();
  const game = useSelector(_ => _.games.game);

  // componentDidMount
  React.useEffect(() => {
    dispatch(gameFetch(gameId));
  }, []);

  return (
    <>
      <NavigationView />
      <div className={styles.root}>
        {game ? <Board tiles={game.state.tiles} /> : null}
      </div>
    </>
  );
};

export default GameView;
