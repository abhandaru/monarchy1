import * as React from 'react';
import classnames from 'classnames';
import Piece from '~/views/GameView/Piece';
import styles from './index.css';
import { useSelector, useDispatch } from 'react-redux';

const vecCompare = (a, b) =>
  a.i === b.i && a.j === b.j;

const Tile = (props) => {
  const { currentPlayerId, tile, size } = props;
  const { piece: pieceOccupying, point } = tile;
  const selections = useSelector(_ => _.games.gameSelections);

  // Determine how this tile should be painted.
  const { turnState, piece: pieceSelected, movements } = selections;
  const pieceSelectedOwned = pieceSelected && (pieceSelected.playerId == currentPlayerId);
  const paintAsMovement = turnState === 'Initial' && movements.some(_ => vecCompare(point, _));

  const styleOverride = {width: size, height: size};
  const className = classnames(
    styles.root,
    pieceSelectedOwned && paintAsMovement && styles.movement,
    !pieceSelectedOwned && paintAsMovement && styles.movementNonOwner
  );

  return (
    <div style={styleOverride} className={className}>
      {pieceOccupying ? <Piece piece={pieceOccupying} point={point} /> : null}
    </div>
  );
}

export default Tile;
