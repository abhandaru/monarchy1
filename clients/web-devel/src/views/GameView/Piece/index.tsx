import * as React from 'react';
import * as Types from '~/util/types';
import classnames from 'classnames';
// @ts-ignore
import styles from './index.css';
import Badge from 'react-bootstrap/Badge';
import Popover from 'react-bootstrap/Popover';
import OverlayTrigger from 'react-bootstrap/OverlayTrigger';
import usePlayer from '~/state-hooks/usePlayer';

const StatusItem = (props) => {
  const { label, children } = props;
  return (
    <div className={styles.statusItem}>
      <div className={styles.statusItemLabel}>{label}</div>
      <div className={styles.statusItemValue}>
        {children}
      </div>
    </div>
  );
};

type DirectionProps = {
  direction: Types.Vec;
  color: string;
};

const Direction = (props: DirectionProps) => {
  const { direction, color } = props;
  const { i, j } = direction;
  const angle = Math.atan2(i, j) * (180 / Math.PI);
  const style = {
    borderLeft: `16px solid ${color}`,
    transform: `rotate(${angle}deg)`,
    marginTop: (-i * 60) + '%',
    marginLeft: (-j * 60) + '%',
  };
  return <div className={styles.direction} style={style} />;
};

export const Piece = (props) => {
  const { piece, point } = props;
  const {
    id,
    order,
    name,
    playerId,
    currentWait,
    currentHealth,
    currentBlocking,
    currentDirection,
    currentFocus,
    currentEffects,
    blockingAjustment
  } = piece;

  // Determine whether or not an avatar has been defined.
  const avatarImage = styles[order];
  const player = usePlayer(playerId);
  const playerColor = player?.user.profile?.color ?? '#FFFFFF';
  const pieceStyle = { border: `3px solid ${playerColor}`};

  // Overlay structure (evolving).
  const overlayEl = (
    <Popover id={`popover-piece-${id}`}>
      <Popover.Header>{name}</Popover.Header>
      <Popover.Body>
        <StatusItem label='Coord'>
          <Badge bg='light'>{point.i}-{point.j}</Badge>
        </StatusItem>
        <StatusItem label='Health'>
          <Badge bg='light'>{currentHealth}</Badge>
        </StatusItem>
        <StatusItem label='Blocking'>
          <Badge bg='light'>{Math.round(100 * currentBlocking)}%</Badge>
        </StatusItem>
        <StatusItem label='Wait'>
            <Badge bg='light'>{currentWait}</Badge>
        </StatusItem>
      </Popover.Body>
    </Popover>
  );

  return (
    <OverlayTrigger trigger={['hover', 'focus']} placement='top' overlay={overlayEl}>
      <div className={styles.root}>
        <div className={classnames(styles.avatar, avatarImage)} style={pieceStyle}>
          {avatarImage == null ? order : null}
          <Direction direction={currentDirection} color={playerColor} />
        </div>
      </div>
    </OverlayTrigger>
  );
}

export default Piece;
