import * as React from 'react';
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

export const Piece = (props) => {
  const { piece } = props;
  const {
    id,
    order,
    name,
    playerId,
    currentWait,
    currentHealth,
    currentBlocking,
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
        <StatusItem label='Player'>
          {player ? <Badge bg='light'>@{player.user.username}</Badge> : null}
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
        </div>
      </div>
    </OverlayTrigger>
  );
}

export default Piece;
