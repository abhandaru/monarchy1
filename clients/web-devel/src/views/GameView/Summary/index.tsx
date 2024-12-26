import * as React from 'react';

import Card from 'react-bootstrap/Card';
// @ts-ignore
import styles from './index.css';
import useMyTurn from '~/state-hooks/useMyTurn';
import { GameSelections } from '~/state/state';
import { useSelector } from 'react-redux';

type Props = {
  selections: GameSelections;
};

const Summary = (props: Props) => {
  const { selections } = props;
  const myTurn = useMyTurn();
  return (
    <div className={styles.root}>
      <Card>
        <h4>Your turn: {myTurn ? 'Yes' : 'No'}</h4>
      </Card>
    </div>
  );
};

export default Summary;
