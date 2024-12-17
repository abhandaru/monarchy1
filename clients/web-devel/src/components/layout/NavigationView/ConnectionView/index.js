import * as React from 'react';
import Badge from 'react-bootstrap/Badge';
import styles from './index.css';
import { useSelector } from 'react-redux'

const ConnectionView = (props) => {
  const latency = useSelector(_ => _.connection.latency);
  const latencyText = latency != null ? latency + 'ms' : '--';
  const latencyVariant = latency <= 50 ? 'primary' : 'warning';

  return (
    <div className={styles.root}>
      <div>
        ping
        {' '}
        <Badge variant={latencyVariant}>{latencyText}</Badge>
      </div>
    </div>
  );
};

export default ConnectionView;
