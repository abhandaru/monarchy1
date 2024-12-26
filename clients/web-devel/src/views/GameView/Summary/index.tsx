import * as React from 'react';

import Badge from 'react-bootstrap/Badge';
import Card from 'react-bootstrap/Card';
import Table from 'react-bootstrap/Table';
// @ts-ignore
import styles from './index.css';
import useCurrentPlayer from '~/state-hooks/useCurrentPlayer';
import useMyTurn from '~/state-hooks/useMyTurn';
import { GameSelections } from '~/state/state';

type Props = {
  selections: GameSelections;
};

const Summary = (props: Props) => {
  const { selections } = props;
  const currentPlayer = useCurrentPlayer();
  const myTurn = useMyTurn();

  const playerEl = currentPlayer ? (
    <>
      {myTurn && <Badge bg='success' text='light'>your move</Badge>}
      {!myTurn && <Badge bg='light' text='dark'>waiting for…</Badge>}
      <p>
        <b>@{currentPlayer.user.username}</b>
      </p>
    </>
  ) : null;

  const { piece } = selections;
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
          {playerEl}
          {pieceEl}
        </Card.Body>
      </Card>
    </div>
  );
};

export default Summary;
