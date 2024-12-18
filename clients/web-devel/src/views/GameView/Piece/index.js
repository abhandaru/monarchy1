import * as React from 'react';
import classnames from 'classnames';
import styles from './index.css';
import { useSelector, useDispatch } from 'react-redux';
import Badge from 'react-bootstrap/Badge';
import Popover from 'react-bootstrap/Popover';
import OverlayTrigger from 'react-bootstrap/OverlayTrigger';

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
    <OverlayTrigger trigger={['hover', 'focus']} placement='top' overlay={overlayEl}>
      <div className={styles.root}>
        <div className={classnames(styles.avatar, avatarImage)}>
          {avatarImage == null ? order : null}
        </div>
      </div>
    </OverlayTrigger>
  );
}

export default Piece;
