import * as React from 'react';
import classnames from 'classnames';
import styles from './index.css';
import { useSelector, useDispatch } from 'react-redux';
import Badge from 'react-bootstrap/Badge';
import Popover from 'react-bootstrap/Popover';
import OverlayTrigger from 'react-bootstrap/OverlayTrigger';
import streamProxy from '~/api/streamProxy';

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

const Piece = (props) => {
  const { piece, point } = props;
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

  // Connection with global state.
  const gameId = useSelector(_ => _.games.game && _.games.game.id);
  const onClick = React.useCallback(() => {
    if (gameId != null) {
      streamProxy.send('GameSelectTile', { gameId, point });
    }
  }, [gameId, point]);

  // Overlay structure (evolving).
  const overlayEl = (
    <Popover id={`popover-piece-${id}`}>
      <Popover.Title as="h3">{name}</Popover.Title>
      <Popover.Content>
        <StatusItem label='Health'>
          <Badge variant='light'>{currentHealth}</Badge>
        </StatusItem>
        <StatusItem label='Wait'>
          <Badge variant='light'>{currentWait}</Badge>
        </StatusItem>
        <StatusItem label='Blocking'>
          <Badge variant='light'>{Math.round(100 * currentBlocking)}%</Badge>
        </StatusItem>
      </Popover.Content>
    </Popover>
  );

  return (
    <OverlayTrigger trigger='hover' placement='top' overlay={overlayEl}>
      <div className={styles.root} onClick={onClick}>
        {order}
      </div>
    </OverlayTrigger>
  );
}

export default Piece;
