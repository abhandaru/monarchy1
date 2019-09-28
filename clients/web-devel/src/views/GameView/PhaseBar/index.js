import * as React from 'react';
import Button from 'react-bootstrap/Button';
import ButtonGroup from 'react-bootstrap/ButtonGroup';
import Card from 'react-bootstrap/Card';
import classnames from 'classnames';
import styles from './index.css';
import { gameSetPhase } from '~/state/actions';
import { useSelector, useDispatch } from 'react-redux';

const Phases = {
  Move: 'Move',
  Attack: 'Attack',
  Turn: 'Turn',
  End: 'End'
};

const PhaseBar = (props) => {
  const { className } = props;
  const dispatch = useDispatch();
  const onSelect = React.useCallback(p => dispatch(gameSetPhase(p)));
  const phase = useSelector(_ => _.games.gameSelections.phase);
  const phaseEls = Object.keys(Phases).map(k => {
    const label = Phases[k];
    const disabled = phase === k;
    return (
      <Button
        key={k}
        size='lg'
        variant='primary'
        disabled={disabled}
        onClick={() => onSelect(k)}>
        {label}
      </Button>
    );
  });

  return (
    <ButtonGroup className={classnames(styles.root, className)}>
      {phaseEls}
    </ButtonGroup>
  );
};

export default PhaseBar;
