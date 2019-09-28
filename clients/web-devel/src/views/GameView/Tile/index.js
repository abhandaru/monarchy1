import * as React from 'react';
import classnames from 'classnames';
import Piece from '~/views/GameView/Piece';
import streamProxy from '~/api/streamProxy';
import styles from './index.css';
import { useSelector, useDispatch } from 'react-redux';

const vecCompare = (a, b) =>
  a.i === b.i && a.j === b.j;

const InactiveTile = (props) => {
  const { children, style, gameId, point } = props;
  const className = classnames(styles.tile, styles.inactive);
  const onClick = React.useCallback(() => {
    if (gameId) {
      streamProxy.send('GameSelectTile', { gameId, point });
    }
  }, [gameId]);

  return (
    <div style={style} className={className} onClick={onClick}>
      {children}
    </div>
  );
};

const MovementTile = (props) => {
  const { style, children, controlled, gameId } = props;
  const className = classnames(
    styles.tile,
    controlled ? styles.movement : styles.movementNonOwner
  );
  return (
    <div style={style} className={className}>
      {children}
    </div>
  );
};

const AttackTile = (props) => {
  const { style, children, controlled, gameId } = props;
  const className = classnames(
    styles.tile,
    controlled ? styles.attack : styles.attackNonOwner
  );
  return (
    <div style={style}  className={className}>
      {children}
    </div>
  );
};

const Tile = (props) => {
  const { currentPlayerId, tile, size } = props;
  const { piece: pieceOccupying, point } = tile;
  const gameId = useSelector(_ => _.games.game && _.games.game.id);
  const selections = useSelector(_ => _.games.gameSelections);

  // Determine how this tile should be painted.
  const { phase, piece: pieceSelected, movements, attacks } = selections;
  const currentControl = pieceSelected && (pieceSelected.playerId == currentPlayerId);
  const paintAsMovement = phase === 'Move' && movements.some(_ => vecCompare(point, _));
  const paintAsAttack = phase === 'Attack' && attacks.some(_ => _.some(_ => vecCompare(point, _)));

  // Pick tile implementation.
  let Component = InactiveTile;
  if (paintAsMovement) Component = MovementTile;
  else if (paintAsAttack) Component = AttackTile;

  // Stack display logic
  const styleOverride = {width: size, height: size};
  return (
    <Component
      style={styleOverride}
      controlled={currentControl}
      gameId={gameId}
      point={point}>
      {pieceOccupying ? <Piece piece={pieceOccupying} /> : null}
    </Component>
  );
}

export default Tile;
