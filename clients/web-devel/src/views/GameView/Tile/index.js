import * as Actions from '~/state/actions';
import * as React from 'react';
import classnames from 'classnames';
import Piece from '~/views/GameView/Piece';
import styles from './index.css';
import writeSelect from '~/api/writeSelect';
import { useSelector, useDispatch } from 'react-redux';

const vecCompare = (a, b) =>
  a.i === b.i && a.j === b.j;

const vecAdd = (a, b) =>
  ({i: a.i + b.i, j: a.j + b.j});

const InactiveTile = (props) => {
  const { children, style, gameId, point } = props;
  const className = classnames(styles.tile, styles.inactive);
  const dispatch = useDispatch();

  const onClick = React.useCallback(() => {
    if (gameId) {
      writeSelect({ gameId, point }).then(r => {
        dispatch(Actions.gameSetSelections(r.data.select));
      });
    }
  }, [gameId, point]);

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

const DirectionTile = (props) => {
  const { style, children, controlled, gameId } = props;
  const className = classnames(
    styles.tile,
    controlled ? styles.direction : styles.directionNonOwner
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
  const { phase, piece: pieceSelected, movements, attacks, directions, selection } = selections;
  const currentControl = pieceSelected && (pieceSelected.playerId == currentPlayerId);
  const paintAsMovement = phase === 'Move' && movements.some(_ => vecCompare(point, _));
  const paintAsAttack = phase === 'Attack' && attacks.some(_ => _.some(_ => vecCompare(point, _)));

  const directionsRel = (selection != null) ? directions.map(_ => vecAdd(selection,  _)) : directions;
  const paintAsDirection = phase === 'Turn' && directionsRel.some(_ => vecCompare(point, _));

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
