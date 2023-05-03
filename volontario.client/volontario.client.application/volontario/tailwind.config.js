/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      colors: {
        textPrimary: '#A1A1A1',
        bgPrimary: 'rgba(9,116,140,0.7)',
        bgPrimaryLight: 'rgba(9,116,140,0.7)',
        bgWhite: '#f2f2f2',
        btnAccentPrimary: '#FBC740',
        btnAccentHover: '#f6c033',
        btnAccentDisabled: 'rgba(175,172,172,0.77)',
        warnColor: 'rgba(255,71,71,0.77)'
      },
    },
  },
  plugins: [],
};
