import * as React from 'react';
import ConnectionView from './ConnectionView';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import { Link } from 'react-router-dom';

const NavigationView = (props) => {
  return (
    <Navbar bg='light' variant='light'>
      <Navbar.Brand href='/'>Monarchy</Navbar.Brand>
      <Navbar.Collapse>
        <Nav className='mr-auto'>
          <Link to='/performance'>Match performance</Link>
        </Nav>
        <ConnectionView />
      </Navbar.Collapse>
    </Navbar>
  );
};

export default NavigationView;
