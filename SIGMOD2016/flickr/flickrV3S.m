set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [723.0073488	808.7218952	908.5332514	1002.246954	1108.12839];
y2 = [74.07521664	229.0138582	405.0850492	570.9872868	745.1956662];
y3 = [10.33418842	17.92119667	25.68730722	31.89414594	37.48174428];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the number of keywords in S');
ylabel('time (ms)');

axis([0.5 5.5 0.0 20000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'1','3','5','7','9'});
leg=legend('basic-g','basic-w','Dec', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);