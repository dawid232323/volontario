/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      colors: {
        textPrimary: '#A1A1A1',
        bgPrimary: '#A44646',
        bgWhite: '#f2f2f2',
        btnAccentPrimary: '#00B8B8',
        btnAccentHover: '#018c8c',
        btnAccentDisabled: 'rgba(1,176,176,0.62)'
      },
    },
  },
  plugins: [],
};
