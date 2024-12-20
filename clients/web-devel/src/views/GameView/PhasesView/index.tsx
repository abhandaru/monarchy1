import * as Actions from '~/state/actions';
import * as React from 'react';
import Button from 'react-bootstrap/Button';
import ButtonGroup from 'react-bootstrap/ButtonGroup';
import useMyTurn from '~/state-hooks/useMyTurn';
import { gameSetPhase } from '~/state/actions';
import { State } from '~/state/state';
import { useSelector, useDispatch } from 'react-redux';

const Phases = {
  MOVE: 'Move',
  ATTACK: 'Attack',
  DIR: 'Turn',
  END: 'End'
};

type Props = {
  className?: string;
  gameId: string;
};

const PhasesView = (props: Props) => {
  const { className, gameId } = props;
  const dispatch = useDispatch();
  const myTurn = useMyTurn();
  const phase = useSelector<State, string>(_ => _.games.gameSelections.phase);
  const phasesAllowed = useSelector<State, string[] | null>(_ =>_.games.game?.state.currentPhases);

  // Callbacks
  const onSelect = React.useCallback(p => dispatch(gameSetPhase(p)), [dispatch]);
  React.useEffect(() => {
    console.log('PhasesView effect:', { phase, myTurn, gameId });
    if (phase === 'END' && myTurn) {
      dispatch(Actions.gameEndTurn(gameId));
    }
  }, [phase, myTurn, gameId]);

  const phaseEls = Object.keys(Phases).map(k => {
    const label = Phases[k];
    const current = phase === k;
    const disabled = current || !phasesAllowed.includes(k);
    return (
      // @ts-ignore
      <Button
        key={k}
        size='lg'
        variant={current ? 'primary' : 'light'}
        disabled={disabled}
        onClick={() => onSelect(k)}>
        {label}
      </Button>
    );
  });

  return (
    // @ts-ignore 
    <ButtonGroup className={className}>
      {phaseEls}
    </ButtonGroup>
  );
};

export default PhasesView;
