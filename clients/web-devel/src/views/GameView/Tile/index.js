import * as Actions from '~/state/actions';
import * as React from 'react';
import classnames from 'classnames';
import InactiveTile from './InactiveTile';
import MovementTile from './MovementTile';
import Piece from '~/views/GameView/Piece';
import styles from './index.css';
import { useSelector } from 'react-redux';

const vecCompare = (a, b) =>
  a.i === b.i && a.j === b.j;

const vecAdd = (a, b) =>
  ({i: a.i + b.i, j: a.j + b.j});

const AttackTile = (props) => {
  const { style, children, controlled, gameId, point } = props;
  const className = classnames(
    styles.tile,
    controlled ? styles.attack : styles.attackNonOwner
  );
  return (
    <div style={style} className={className}>
      {children}
    </div>
  );
};

const DirectionTile = (props) => {
  const { style, children, controlled, gameId, point } = props;
  const className = classnames(
    styles.tile,
    controlled ? styles.direction : styles.directionNonOwner
  );
  return (
    <div style={style} className={className}>
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
  const { phase, piece: pieceSelected, movements, attacks, directions, selection } = selections;

  const currentControl = pieceSelected && (pieceSelected.playerId == currentPlayerId);
  const paintAsMovement = phase === 'MOVE' && movements.some(_ => vecCompare(point, _));
  const paintAsAttack = phase === 'ATTACK' && attacks.some(_ => _.some(_ => vecCompare(point, _)));

  const directionsRel = (selection != null) ? directions.map(_ => vecAdd(selection,  _)) : directions;
  const paintAsDirection = phase === 'DIR' && directionsRel.some(_ => vecCompare(point, _));

  // Pick tile implementation.
  let Component = InactiveTile;
  if (paintAsMovement) Component = MovementTile;
  else if (paintAsAttack) Component = AttackTile;
  else if (paintAsDirection) Component = DirectionTile;

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
