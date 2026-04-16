import React from 'react';
import './Sidebar.css';

const Sidebar = ({ items, active, onSelect }) => {
  return (
    <aside className="sidebar">
      {items.map((item) => (
        <button
          key={item.key}
          className={`sidebar-item ${active === item.key ? 'active' : ''}`}
          onClick={() => onSelect(item.key)}
        >
          <span className="sidebar-icon">{item.icon}</span>
          <span className="sidebar-label">{item.label}</span>
        </button>
      ))}
    </aside>
  );
};

export default Sidebar;
