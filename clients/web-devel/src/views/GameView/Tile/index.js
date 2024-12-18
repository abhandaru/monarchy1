import * as React from 'react';
import * as vector from '~/util/vector';
import AttackTile from './AttackTile';
import DirectionTile from './DirectionTile';
import EffectTile from './EffectTile';
import InactiveTile from './InactiveTile';
import MovementTile from './MovementTile';
import Piece from '~/views/GameView/Piece';
import { useSelector } from 'react-redux';


const Tile = (props) => {
  const { playerId, tile, size } = props;
  const { piece: pieceOccupying, point } = tile;
  const gameId = useSelector(_ => _.games.game && _.games.game.id);
  const selections = useSelector(_ => _.games.gameSelections);

  // Determine how this tile should be painted.
  const { phase, piece: pieceSelected, movements, attacks, directions, selection, effects } = selections;

  // For now use `filter` to find matching attacks and then use some dumb
  // mean-distance calculation to compute the "canonical" attack for that point
  const attackOverlap = attacks.filter(_ => _.some(_ => vector.compare(point, _)));
  const attackCanonical = attacks.sort((a, b) => vector.meanSquareDistance(point, a) - vector.meanSquareDistance(point, b))[0];

  // Figure out how what state this tile is in. Consider hoisting into the
  // `GameView` and just pass this context down (much cheaper, unless
  // memoization is used here).
  const currentControl = pieceSelected && (pieceSelected.playerId == playerId);
  const paintAsMovement = phase === 'MOVE' && movements.some(_ => vector.compare(point, _));
  const paintAsAttack = phase === 'ATTACK' && attackOverlap.length;
  const paintAsEffect = phase === 'ATTACK' && effects.some(_ => vector.compare(point, _.point));

  // Determine if this tile should be painted as a direction.
  const directionCanonical = selection && directions.find(_ => vector.compare(point, vector.add(selection,  _)));
  const paintAsDirection = phase === 'DIR' && directionCanonical;

  // Pick tile implementation.
  let Component = InactiveTile;
  if (paintAsMovement) Component = MovementTile;
  else if (paintAsEffect) Component = EffectTile;
  else if (paintAsAttack) Component = AttackTile;
  else if (paintAsDirection) Component = DirectionTile;

  // Stack display logic
  const styleOverride = {width: size, height: size};
  return (
    <Component
      style={styleOverride}
      controlled={currentControl}
      gameId={gameId}
      point={point}
      attack={attackCanonical}
      direction={directionCanonical}>
      {pieceOccupying ? <Piece piece={pieceOccupying} /> : null}
    </Component>
  );
}

export default Tile;
