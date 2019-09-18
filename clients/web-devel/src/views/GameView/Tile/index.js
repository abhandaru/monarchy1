import * as React from 'react';
import classnames from 'classnames';
import Piece from '~/views/GameView/Piece';
import styles from './index.css';
import { useSelector, useDispatch } from 'react-redux';

const vecCompare = (a, b) =>
  a.i === b.i && a.j === b.j;

const Tile = (props) => {
  const { tile, size } = props;
  const { piece, point } = tile;

  const selections = useSelector(_ => _.games.gameSelections);
  const { turnState, movements } = selections;

  const moveOption = turnState === 'Initial' && movements.some(_ => vecCompare(point, _));

  const styleOverride = {width: size, height: size};
  const className = classnames(
    styles.root,
    moveOption && styles.movement
  );

  return (
    <div style={styleOverride} className={className}>
      {piece ? <Piece piece={piece} point={point} /> : null}
    </div>
  );
}

export default Tile;
