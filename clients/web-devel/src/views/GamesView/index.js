import * as React from 'react';
import Badge from 'react-bootstrap/Badge';
import Button from 'react-bootstrap/Button';
import fetchGames from './fetchGames';
import styles from './index.css';
import Table from 'react-bootstrap/Table';
import { gamesSetRecent } from '~/state/actions';
import { useSelector, useDispatch } from 'react-redux';
import { withRouter } from 'react-router-dom'

const GameRow = (props) => {
  const { viewerId, game, onView } = props;
  const { id, status, players } = game;
  const onViewClick = React.useCallback(() => onView(id));
  // Property formatting
  const opponent = players.filter(_ => _.user.id != viewerId)[0];
  const opponentName = opponent ? opponent.user.username : '–';
  const opponentRating = opponent ? opponent.user.rating : '–';
  const statusBg = status == 'Started' ? 'success' : 'secondary';
  return (
    <tr>
      <td className={styles.opponentCell}>
        <div className={styles.opponent}>
          <div>{opponentName} @ {opponentRating}</div>
          <Button variant='outline-primary' size='sm' onClick={onViewClick}>View</Button>
        </div>
      </td>
      <td>
        <Badge bg={statusBg} text='light'>{status}</Badge>
      </td>
    </tr>
  );
};

const GamesTable = withRouter((props) => {
  const { viewerId, games, history } = props;
  const onView = React.useCallback((id) => history.push('/games/' + id));
  return (
    <div>
      <h3>Active games</h3>   
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Opponent</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>{
          games.map(g =>
            <GameRow
              key={g.id}
              viewerId={viewerId}
              onView={onView}
              game={g}
            />
          )
        }</tbody>
      </Table>
    </div>
  );
});

const GamesView = (props) => {
  const dispatch = useDispatch();
  const userId = useSelector(_ => _.auth.userId);
  const recent = useSelector(_ => _.games.recent);
  const active = recent.filter(_ => _.status === 'Started');
  // onComponentDidMount, load data
  React.useEffect(() => {
    const query = { userId };
    fetchGames(query).then(_ => dispatch(gamesSetRecent(_.data.games)))
  }, []);
  // Conditionally render games, if any.
  return active.length > 0 ? <GamesTable viewerId={userId} games={active} /> : null;
};

export default GamesView;
