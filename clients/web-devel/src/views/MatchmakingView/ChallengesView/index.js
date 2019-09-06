import * as React from 'react';
import Button from 'react-bootstrap/Button';
import streamProxy from '~/api/streamProxy';
import Table from 'react-bootstrap/Table';
import { useSelector } from 'react-redux'
import styles from './index.css';

const ChallengeView = (props) => {
  const { challenge } = props;
  const userId = useSelector(_ => _.auth.userId);
  const onCancel = React.useCallback(() => streamProxy.send({name: 'ChallengeSeekCancel'}));
  const onAccept = React.useCallback(() => {
    const body = JSON.stringify({ opponentId: challenge.userId });
    streamProxy.send({name: 'ChallengeAccept', body });
  });
  // Allow user to cancel their own seek, or accept another.
  const action = userId == challenge.userId ?
    <Button variant='outline-secondary' size='sm' onClick={onCancel}>Cancel</Button> :
    <Button variant='outline-primary' size='sm' onClick={onAccept}>Accept</Button>;
  // Render the row.
  return (
    <tr>
      <td>
        <div className={styles.nameCell}>
          <div>{challenge.username}</div>
          {action}
        </div>
      </td>
      <td>1000?</td>
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
        {challenges.map(c => <ChallengeView key={c.userId} challenge={c} />)}
      </tbody>
    </Table>
  );
};

export default ChallengesView;
