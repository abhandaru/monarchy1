import * as React from 'react';
import Button from 'react-bootstrap/Button';
import streamProxy from '~/api/streamProxy';
import styles from './index.css';
import Table from 'react-bootstrap/Table';
import { useSelector } from 'react-redux'

const ChallengeView = (props) => {
  const { challenge } = props;
  const userId = useSelector(_ => _.auth.userId);
  const onCancel = React.useCallback(() => streamProxy.send('ChallengeSeekCancel'));
  const onAccept = React.useCallback(() => {
    const body = { opponentId: challenge.host.id };
    streamProxy.send('ChallengeAccept', body);
  });
  // Allow user to cancel their own seek, or accept another.
  const action = userId == challenge.host.id ?
    <Button variant='outline-secondary' size='sm' onClick={onCancel}>Cancel</Button> :
    <Button variant='outline-primary' size='sm' onClick={onAccept}>Accept</Button>;
  // Render the row.
  return (
    <tr>
      <td className={styles.nameCell}>
        <div className={styles.name}>
          <div>{challenge.host.username}</div>
          {action}
        </div>
      </td>
      <td>{challenge.host.rating}</td>
    </tr>
  );
};

const ChallengesView = (props) => {
  const { challenges, className } = props;
  return (
    <Table striped bordered hover className={className}>
      <thead>
        <tr>
          <th>Open challenges</th>
          <th>Rating</th>
        </tr>
      </thead>
      <tbody>
        {challenges.map(c => <ChallengeView key={c.host.id} challenge={c} />)}
      </tbody>
    </Table>
  );
};

export default ChallengesView;
