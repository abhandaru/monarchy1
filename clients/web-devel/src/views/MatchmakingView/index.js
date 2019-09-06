import * as React from 'react';
import Button from 'react-bootstrap/Button';
import streamProxy from '~/api/streamProxy';
import Table from 'react-bootstrap/Table';
import { useSelector } from 'react-redux'

const ChallengeView = (props) => {
  const { challenge } = props;
  const userId = useSelector(_ => _.auth.userId);
  const cancel = userId == challenge.userId ?
    <Button variant='outline-secondary' size='sm'>Cancel</Button> : null;
  return (
    <tr>
      <td>{challenge.username} {cancel}</td>
      <td>1000?</td>
    </tr>
  );
};

const MatchmakingView = () => {
  const onSeek = React.useCallback(() => streamProxy.send({name: 'ChallengeSeek'}));
  const userId = useSelector(_ => _.auth.userId);
  const challenges = useSelector(_ => _.matchmaking.challenges);

  const challengeTable = challenges.length > 0 ? (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>Username</th>
          <th>Rating</th>
        </tr>
      </thead>
      <tbody>
        {challenges.map(c => <ChallengeView key={c.userId} challenge={c} />)}
      </tbody>
    </Table>
  ) : null;

  return (
    <div>
      <h3>Matchmaking</h3>
      <Button onClick={onSeek}>Seek challenge</Button>
      {challengeTable}
    </div>
  );
};

export default MatchmakingView;
