import * as React from 'react';
import * as Types from '~/util/types';
import Badge from 'react-bootstrap/Badge';
import Card from 'react-bootstrap/Card';
import Table from 'react-bootstrap/Table';
// @ts-ignore
import styles from './index.css';
import useMyPlayer from '~/state-hooks/useMyPlayer';
import useMyTurn from '~/state-hooks/useMyTurn';
import { Game } from '~/state/state';

type Props = {
  game: Game;
};

const GameStatus = (props: { status: string }) => {
  const bg = props.status === 'COMPLETE' ? 'primary' : 'secondary';
  return (
    <Badge bg={bg} text='light' className={styles.gameStatus}>
      {props.status}
    </Badge>
  );
};

const Player = (props: { player: Types.Player }) => {
  const { player } = props;
  const myTurn = useMyTurn();
  const myPlayer = useMyPlayer();

  let statusEl = null;
  if (player.status === 'WON') statusEl = <Badge bg='success' text='light'>won</Badge>;
  else if (player.status === 'LOST') statusEl = <Badge bg='danger' text='light'>lost</Badge>;
  else if (player.status === 'DRAWN') statusEl = <Badge bg='secondary' text='light'>drawn</Badge>;
  else statusEl = (
    <>
      {player.id === myPlayer.id ? <Badge bg='primary' text='light'>you</Badge> : null}
      {player.id === myPlayer.id && myTurn ? <Badge bg='success' text='light'>playing</Badge> : null}
    </>
  );

  return (
    <div className={styles.player} key={player.id}>
      <div>{player.user.username}</div>
      {statusEl}
    </div>
  );
};

const Summary = (props: Props) => {
  const { game } = props;
  const playerEls = game.players.map(player => <Player player={player} key={player.id} />);

  const { piece } = game.state.currentSelection;
  const pieceEl = piece ? (
    <Table striped bordered size='sm'>
      <tbody>
        <tr>
          <td>Name</td>
          <td>{piece.name}</td>
        </tr>
        <tr>
          <td>Health</td>
          <td>{piece.currentHealth}</td>
        </tr>
        <tr>
          <td>Wait</td>
          <td>{piece.currentWait}</td>
        </tr>
      </tbody>
    </Table>
  ) : null;

  return (
    <div className={styles.root}>
      <Card>
        <Card.Body>
          <GameStatus status={game.status} />
          <div className={styles.players}>
            {playerEls}
          </div>
          {pieceEl}
        </Card.Body>
      </Card>
    </div>
  );
};

export default Summary;
