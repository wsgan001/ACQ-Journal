set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [2731.73955472	2662.28954394	2663.54120974	2679.2459535	2694.07776644];
y2 = [0.15519977	0.13421035	0.14121023	0.13559179	0.13488156];
y3 = [51.50822718	30.67522841	23.40001221	19.628765349999998	18.55069213];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the core number of the vertex');
ylabel('time (ms)');

axis([0.5 5.5 0.1 10000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'5','10','15','20','25'});
leg=legend('rebuild','insert','delete', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);